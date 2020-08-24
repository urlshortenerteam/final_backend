package service

import (
	"time"
	"github.com/violedo/logService/dao"
	"github.com/violedo/logService/entity"
	idao "github.com/violedo/logService/interface/dao"	
	log "github.com/sirupsen/logrus"
)

//VisitService service to insert the visitLog into DB
type VisitService struct {
	logDAO idao.DAO
}

//InitService initiation of the service
func (v *VisitService) InitService(logDAO ...idao.DAO){
	if len(logDAO) == 0 {
		logDAO=append(logDAO,&dao.LogDAO{})
	}
	v.logDAO = logDAO[0]
	v.logDAO.InitDB()
}

//Destr destruction of the service
func (v *VisitService)Destr(){
	v.logDAO.Destr()
}

//Log insert the visitLog into visitLog and changes data in user and shortenLog
//ShortenerID string,IP string,Device bool,owner uint64,shortenID uint64
func (v VisitService)Log(shortURL string, IP string, Device bool){
	shortenID , shortenerID , err :=v.logDAO.ByShortURL(shortURL)
	entity :=entity.Visit{ShortenerID:ShortenerID,
							VisitTime:time.Now(),
							IP:IP,
							Device:Device}
	err := v.logDAO.InsertLog(entity)
	if err !=nil {
		log.Info(err.Error())
	}
	err =v.logDAO.UpdateUser(owner)
	if err !=nil {
		log.Info(err.Error())
	}
	err =v.logDAO.UpdateShorten(shortenID)
	if err !=nil {
		log.Info(err.Error())
	}
}