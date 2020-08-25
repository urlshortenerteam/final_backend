package service

import (
	"database/sql"
	"math/rand"
	"time"

	"github.com/ao7777/redirectService/dao"
	"github.com/ao7777/redirectService/entity"
	idao "github.com/ao7777/redirectService/interface/dao"

	log "github.com/sirupsen/logrus"
)

// Redirect ...
// @ao7777
// RedirectService
type Redirect struct {
	shortDAO idao.IShortUrl
}

// Init use it before service
func (r *Redirect) Init(shortDAO ...idao.IShortUrl) {
	if len(shortDAO) == 0 {
		shortDAO=append(shortDAO,&dao.ShortDAO{})
	}
	r.shortDAO = shortDAO[0]
	r.shortDAO.InitShortDAO()
}

// ShortToLong Convert shortUrl to its destination
func (r *Redirect) ShortToLong(shortURL string) (longURL entity.MongoShort) {
	shortEntity, err := r.shortDAO.ByShortURL(shortURL)
	if err != nil && err != sql.ErrNoRows {
		log.Fatal(err.Error())
	}
	if err == sql.ErrNoRows{
		return entity.MongoShort{ShortenID:-1,LongID:"",LongURL:"NOTFOUND"}
	}
	var filteredURL []entity.MongoShort
	cnt := 0
	for _, longU := range shortEntity.LongURLs {
		if longU.LongURL == "BANNED" {
			cnt++
			continue
		}
		if longU.LongURL == "LIFT" {
			cnt--
			continue
		}
		filteredURL = append(filteredURL, longU)
	}
	if cnt > 0 {
		return entity.MongoShort{ShortenID:-1,LongID:"",LongURL:"BANNED"}
	}
	random := rand.New(rand.NewSource(time.Now().UnixNano()))
	longURL = filteredURL[random.Int()%len(filteredURL)]
	log.WithField("longUrl", longURL).Info("Redirect service returning")
	return longURL
}
//Destr destructor for Redirect
func (r *Redirect)Destr(){
	r.shortDAO.Destr()
}
