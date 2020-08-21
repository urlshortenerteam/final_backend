package dao

import (
    "database/sql"
	log "github.com/sirupsen/logrus"
	"os"
	"github.com/joho/godotenv"
	"github.com/violedo/logService/entity"
	//
    _ "github.com/go-sql-driver/mysql"
)

var mysqlURL string

//LogDAO implementation of DAO
type LogDAO struct {
	entity.Visit
	db *sql.DB
}

func init() {
	// 日志格式化为 JSON 而不是默认的 ASCII
	log.SetFormatter(&log.JSONFormatter{})

	// 输出 stdout 而不是默认的 stderr，也可以是一个文件
	log.SetOutput(os.Stdout)
	err:=godotenv.Load(os.Getenv("TEST_DIR")+"credentials.env")
	if err != nil{
		log.Fatal(err)
	}
	mysqlURL = os.Getenv("MYSQL_URL")
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
func (l *LogDAO) InsertLog(v entity.Visit) (err error){
	if l.db == nil {
		panic("MYSQL UNINITIALIZED!")
	}
	log.WithFields(log.Fields{
		"ShortenerID":v.ShortenerID,
		"VisitTime":v.VisitTime,
		"IP":v.IP,
		"Device":v.Device,
	}).Info("Start inserting visitLog.")
	//prepary the sql instruction
	stmt, err := l.db.Prepare("INSERT visit_log SET shortener_id=?,visit_time=?,ip=?,device=?")
    if err != nil {
		return
	}
	defer stmt.Close()
	//do the insertion
	res, err := stmt.Exec(v.ShortenerID,v.VisitTime,v.IP,v.Device)
    if err != nil {
		return
	}

	id, err := res.LastInsertId()
	if err != nil {
		return
	}
	log.WithFields(log.Fields{
		"ID":id,
		"ShortenerID":v.ShortenerID,
		"VisitTime":v.VisitTime,
		"IP":v.IP,
		"Device":v.Device,
	}).Info("VisitLog inserted successfully.")
	return
}

//UpdateUser increase the user's visitCount by 1
func (l *LogDAO) UpdateUser(ID uint64) (err error){
	if l.db == nil {
		panic("MYSQL UNINITIALIZED!")
	}
	log.WithFields(log.Fields{
		"ID":ID,
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
	_,err = stmt.Exec(visitCount+1,ID)
	if err != nil {
		return
	}
	log.WithFields(log.Fields{
		"ID":ID,
		"VisitCount":visitCount+1,
	}).Info("User visitCount updated successfully.")
	return
}

//UpdateShorten increase the shortenLog's visitCount by 1
func (l *LogDAO) UpdateShorten(ID uint64) (err error){
	if l.db == nil {
		panic("MYSQL UNINITIALIZED!")
	}
	log.WithFields(log.Fields{
		"ID":ID,
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
	_,err = stmt.Exec(visitCount+1,ID)
	if err != nil {
		return
	}
	log.WithFields(log.Fields{
		"ID":ID,
		"VisitCount":visitCount+1,
	}).Info("ShortenLog visitCount updated successfully.")
	return
}