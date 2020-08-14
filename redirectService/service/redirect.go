package service

import (
	"database/sql"
	"math/rand"
	"time"

	"github.com/ao7777/redirectService/dao"
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
func (r *Redirect) ShortToLong(shortURL string) (longURL string) {
	shortEntity, err := r.shortDAO.ByShortURL(shortURL)
	if err != nil && err != sql.ErrNoRows {
		log.Fatal(err.Error())
	}
	if err == sql.ErrNoRows{
		return "NOTFOUND"
	}
	var filteredURL []string
	cnt := 0
	for _, longU := range shortEntity.LongUrl {
		if longU == "BANNED" {
			cnt++
			continue
		}
		if longU == "LIFT" {
			cnt--
			continue
		}
		filteredURL = append(filteredURL, longU)
	}
	if cnt > 0 {
		return "BANNED"
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
