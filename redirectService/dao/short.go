package dao

import (
	"database/sql"
	"github.com/ao7777/redirectService/entity"
	log "github.com/sirupsen/logrus"
	"os"
	"github.com/joho/godotenv"
	// "time"
	_ "github.com/go-sql-driver/mysql"
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
)

var (
	mongoURL string
	mysqlURL string
)

// ShortDAO implementation of DAO, Init before use
type ShortDAO struct {
	entity.ShortUrl
	db *sql.DB
}

// MongoShort structure for MongoDB
type MongoShort struct {
	ShortenID int64  `bson:"shortenId" json:"shortenId"`
	LongURL   string `bson:"longUrl" json:"longUrl"`
}

func init() {
	// 日志格式化为 JSON 而不是默认的 ASCII
	log.SetFormatter(&log.JSONFormatter{})

	// 输出 stdout 而不是默认的 stderr，也可以是一个文件
	log.SetOutput(os.Stdout)
	err:=godotenv.Load("../credentials.env")
	if err != nil{
		log.Fatal(err)
	}
	mongoURL = os.Getenv("MONGO_URL")
	mysqlURL = os.Getenv("MYSQL_URL")
}

//InitShortDAO init database
func (s *ShortDAO) InitShortDAO() error {
	var err error
	s.db, err = sql.Open("mysql", mysqlURL)
	if err != nil {
		return err
	}
	log.Info("Successfully connected to MySQL.")
	return nil
}

//ByID get a ShortUrl entity by its id
func (s ShortDAO) ByID(id int) (e entity.ShortUrl, err error) {
	if s.db == nil {
		panic("UNINITIALIZED.")
	}
	log.WithFields(log.Fields{
		"id": id,
	}).Info("Start querying MySQL.")
	stmt, err := s.db.Prepare("SELECT id, short_url FROM shorten_log WHERE id = ?")
	if err != nil {
		return e, err
	}
	defer stmt.Close()
	err = stmt.QueryRow(id).Scan(&e.ID, &e.Short)
	if err != nil {
		return e, err
	}
	session, err := mgo.Dial(mongoURL)
	if err != nil {
		return e, err
	}
	defer session.Close()
	c := session.DB("url").C("shortener")
	var results []MongoShort
	log.WithFields(log.Fields{
		"id": e.ID,
	}).Info("Start querying MongoDB.")
	err = c.Find(bson.M{"shortenId": e.ID}).All(&results)
	if err != nil {
		return e, err
	}
	for _, record := range results {
		e.LongUrl = append(e.LongUrl, record.LongURL)
	}
	log.WithFields(log.Fields{
		"id":       e.ID,
		"longUrl":  e.LongUrl,
		"shortUrl": e.Short,
	}).Info("Fetched Item successfully.")
	return e, nil
}

//ByShortURL get ShortUrl entity by short
func (s ShortDAO) ByShortURL(shortURL string) (e entity.ShortUrl, err error) {
	if s.db == nil {
		panic("UNINITIALIZED MySQL connection.")
	}
	stmt, err := s.db.Prepare("SELECT id, short_url FROM shorten_log WHERE short_url = ?")
	if err != nil {
		return e, err
	}
	defer stmt.Close()
	err = stmt.QueryRow(shortURL).Scan(&e.ID, &e.Short)
	if err != sql.ErrNoRows && err != nil {
		return e, err
	}
	if err == sql.ErrNoRows {
		e.LongUrl = append(e.LongUrl, "NOTFOUND")
		return e, err
	}

	session, err := mgo.Dial(mongoURL)
	if err != nil {
		return e, err
	}
	defer session.Close()
	c := session.DB("url").C("shortener")
	var results []MongoShort
	err = c.Find(bson.M{"shortenId": e.ID}).All(&results)
	if err != nil {
		return e, err
	}
	for _, record := range results {
		e.LongUrl = append(e.LongUrl, record.LongURL)
	}
	log.WithFields(log.Fields{
		"id":       e.ID,
		"longUrl":  e.LongUrl,
		"shortUrl": e.Short,
	}).Info("Fetched Item successfully.")
	return e, nil
}

//Destr destruct function
func (s *ShortDAO) Destr() {
	log.Info("Destructing ShortDAO")
	s.db.Close()
}