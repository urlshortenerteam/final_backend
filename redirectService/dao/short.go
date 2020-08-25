package dao

import (
	"database/sql"
	"os"
	"time"
	"strconv"
	"github.com/ao7777/redirectService/entity"
	_ "github.com/go-sql-driver/mysql" //init mysql
	"github.com/joho/godotenv"
	"github.com/patrickmn/go-cache"
	log "github.com/sirupsen/logrus"
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
)

var (
	mongoURL string
	mysqlURL string
)

const maxCacheSize int = 50

// ShortDAO implementation of DAO, Init before use
type ShortDAO struct {
	entity.ShortURL
	db    *sql.DB
	cache *cache.Cache
}


func init() {
	// 日志格式化为 JSON 而不是默认的 ASCII
	log.SetFormatter(&log.JSONFormatter{})

	// 输出 stdout 而不是默认的 stderr，也可以是一个文件
	log.SetOutput(os.Stdout)
	err := godotenv.Load(os.Getenv("TEST_DIR") + "credentials.env")
	if err != nil {
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
	// Create a cache with a default expiration time of 5 minutes, and which
	// purges expired items every 10 minutes
	s.cache = cache.New(5*time.Minute, 10*time.Minute)
	return nil
}

//ByID get a ShortUrl entity by its id
func (s ShortDAO) ByID(id int) (e entity.ShortURL, err error) {
	if x, found := s.cache.Get(strconv.Itoa(id)); found {
		return x.(entity.ShortURL), nil
	}
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
	var results []entity.MongoShort
	log.WithFields(log.Fields{
		"id": e.ID,
	}).Info("Start querying MongoDB.")
	err = c.Find(bson.M{"shortenId": e.ID}).All(&results)
	if err != nil {
		return e, err
	}
	for _, record := range results {
		e.LongURLs = append(e.LongURLs, record)
	}
	log.WithFields(log.Fields{
		"id":       e.ID,
		"longUrl":  e.LongURLs,
		"shortUrl": e.Short,
	}).Info("Fetched Item successfully.")
	if s.cache.ItemCount() <= maxCacheSize {
		s.cache.Set(strconv.Itoa(id), e, cache.DefaultExpiration)
	}
	return e, nil
}

//ByShortURL get ShortUrl entity by short
func (s ShortDAO) ByShortURL(shortURL string) (e entity.ShortURL, err error) {
		if x, found := s.cache.Get(shortURL); found {
		return x.(entity.ShortURL), nil
	}
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
		e.LongURLs = append(e.LongURLs, entity.MongoShort{ShortenID:-1,LongID: "",LongURL: "NOTFOUND"})
		return e, err
	}

	session, err := mgo.Dial(mongoURL)
	if err != nil {
		return e, err
	}
	defer session.Close()
	c := session.DB("url").C("shortener")
	var results []entity.MongoShort
	err = c.Find(bson.M{"shortenId": e.ID}).All(&results)
	if err != nil {
		return e, err
	}
	for _, record := range results {
		e.LongURLs = append(e.LongURLs, record)
	}
	log.WithFields(log.Fields{
		"id":       e.ID,
		"longUrl":  e.LongURLs,
		"shortUrl": e.Short,
	}).Info("Fetched Item successfully.")
		if s.cache.ItemCount() <= maxCacheSize {
		s.cache.Set(shortURL, e, cache.DefaultExpiration)
	}
	return e, nil
}

//Destr destruct function
func (s *ShortDAO) Destr() {
	log.Info("Destructing ShortDAO")
	s.db.Close()
}
