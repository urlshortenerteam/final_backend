package dao

import (
	"database/sql"
	"os"

	"github.com/joho/godotenv"
	log "github.com/sirupsen/logrus"
	"github.com/violedo/logService/entity"

	//
	_ "github.com/go-sql-driver/mysql"
	// "gopkg.in/mgo.v2"
	// "gopkg.in/mgo.v2/bson"
)

var mysqlURL string
var mongoURL string

//LogDAO implementation of DAO
type LogDAO struct {
	entity.Visit
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
	err := godotenv.Load(os.Getenv("TEST_DIR") + "credentials.env")
	if err != nil {
		log.Fatal(err)
	}
	mysqlURL = os.Getenv("MYSQL_URL")
	mongoURL = os.Getenv("MONGO_URL")
}

//InitDB init database
func (l *LogDAO) InitDB() error {
	var err error
	l.db, err = sql.Open("mysql", mysqlURL)
	if err != nil {
		return err
	}
	log.Info("Successfully connected to MySQL.")
	return nil
}

//Destr destruct function
func (l *LogDAO) Destr() {
	log.Info("Destructing LogDAO")
	l.db.Close()
}

//InsertLog write visitLog into DB
func (l *LogDAO) InsertLog(v entity.Visit) (err error) {
	if l.db == nil {
		panic("MYSQL UNINITIALIZED!")
	}
	log.WithFields(log.Fields{
		"ShortenerID": v.ShortenerID,
		"VisitTime":   v.VisitTime,
		"IP":          v.IP,
		"Device":      v.Device,
	}).Info("Start inserting visitLog.")
	//prepary the sql instruction
	stmt, err := l.db.Prepare("INSERT visit_log SET shortener_id=?,visit_time=?,ip=?,device=?")
	if err != nil {
		return
	}
	defer stmt.Close()
	//do the insertion
	res, err := stmt.Exec(v.ShortenerID, v.VisitTime, v.IP, v.Device)
	if err != nil {
		return
	}

	id, err := res.LastInsertId()
	if err != nil {
		return
	}
	log.WithFields(log.Fields{
		"ID":          id,
		"ShortenerID": v.ShortenerID,
		"VisitTime":   v.VisitTime,
		"IP":          v.IP,
		"Device":      v.Device,
	}).Info("VisitLog inserted successfully.")
	return
}

//UpdateUser increase the user's visitCount by 1
func (l *LogDAO) UpdateUser(ID uint64) (err error) {
	if l.db == nil {
		panic("MYSQL UNINITIALIZED!")
	}
	log.WithFields(log.Fields{
		"ID": ID,
	}).Info("Start updating user.")
	stmt, err := l.db.Prepare("SELECT visit_count FROM users WHERE id = ?")
	if err != nil {
		return
	}
	defer stmt.Close()
	var visitCount uint64
	err = stmt.QueryRow(ID).Scan(&visitCount)
	if err != nil {
		return
	}
	stmt, err = l.db.Prepare("update users set visit_count=? where id=?")
	if err != nil {
		return
	}
	_, err = stmt.Exec(visitCount+1, ID)
	if err != nil {
		return
	}
	log.WithFields(log.Fields{
		"ID":         ID,
		"VisitCount": visitCount + 1,
	}).Info("User visitCount updated successfully.")
	return
}

//UpdateShorten increase the shortenLog's visitCount by 1
func (l *LogDAO) UpdateShorten(ID int64) (err error) {
	if l.db == nil {
		panic("MYSQL UNINITIALIZED!")
	}
	log.WithFields(log.Fields{
		"ID": ID,
	}).Info("Start updating shortenLog.")
	stmt, err := l.db.Prepare("SELECT visit_count FROM shorten_log WHERE id = ?")
	if err != nil {
		return
	}
	defer stmt.Close()
	var visitCount uint64
	err = stmt.QueryRow(ID).Scan(&visitCount)
	if err != nil {
		return
	}
	stmt, err = l.db.Prepare("update shorten_log set visit_count=? where id=?")
	if err != nil {
		return
	}
	_, err = stmt.Exec(visitCount+1, ID)
	if err != nil {
		return
	}
	log.WithFields(log.Fields{
		"ID":         ID,
		"VisitCount": visitCount + 1,
	}).Info("ShortenLog visitCount updated successfully.")
	return
}

//ByShortenID get data by shortenID
func (l LogDAO) ByShortenID(shortenID int64) (owner uint64, err error){
	if l.db == nil {
		panic("UNINITIALIZED MySQL connection.")
	}
	stmt, err := l.db.Prepare("SELECT creator_id FROM shorten_log WHERE id = ?")
	if err != nil {
		return 0, err
	}
	defer stmt.Close()
	err = stmt.QueryRow(shortenID).Scan(&owner)
	if err != sql.ErrNoRows && err != nil {
		return 0, err
	}
	if err == sql.ErrNoRows {
		return 0, err
	}

	return owner, nil
}

//ByShortURL get data by shortURL
func (l LogDAO) ByShortURL(shortURL string) (shortenID uint64, owner uint64, shortenerID string, err error) {
	if l.db == nil {
		panic("UNINITIALIZED MySQL connection.")
	}
	stmt, err := l.db.Prepare("SELECT id, creator_id FROM shorten_log WHERE short_url = ?")
	if err != nil {
		return 0, 0, "", err
	}
	defer stmt.Close()
	err = stmt.QueryRow(shortURL).Scan(&shortenID, &owner)
	if err != sql.ErrNoRows && err != nil {
		return 0, 0, "", err
	}
	if err == sql.ErrNoRows {
		return 0, 0, "", err
	}

	// session, err := mgo.Dial(mongoURL)
	// if err != nil {
	// 	return 0, "", err
	// }
	// defer session.Close()
	// c := session.DB("url").C("shortener")
	// var results []MongoShort
	// err = c.Find(bson.M{"shortenId": shortenID}).All(&results)
	// if err != nil {
	// 	return 0, "", err
	// }
	// for _, record := range results {
	// 	e.LongUrl = append(e.LongUrl, record.LongURL)
	// }
	// log.WithFields(log.Fields{
	// 	"id":       e.ID,
	// 	"longUrl":  e.LongUrl,
	// 	"shortUrl": e.Short,
	// }).Info("Fetched Item successfully.")
	return shortenID, owner, "", nil
}
