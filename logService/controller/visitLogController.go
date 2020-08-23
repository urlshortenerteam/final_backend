package controller

import (
	isrv "github.com/violedo/logService/interface/service"
	log "github.com/sirupsen/logrus"
	"net/http"
	"sync"
	"encoding/json"
)
//VisitLogController the visitLog controller
type VisitLogController struct {
	visitLogService isrv.ILogService
}

type postBody struct {
	Device bool `json:"device"`
	OwnerID uint64 `json:"ownerId"`
	ShortenID uint64 `json:"shortenId"`
	IP string `json:"ip"`
	ShortenerID string `json:"shortenerId"`
}

//Init initialization of the controller
func (v *VisitLogController) Init(wg *sync.WaitGroup, visitLogService isrv.ILogService) *http.Server {
	srv := &http.Server{Addr: ":9092"}
	v.visitLogService = visitLogService
	http.HandleFunc("/visitLog", v.serveLog) // 设置访问的路由
	go func() {
		defer wg.Done() // let main know we are done cleaning up

		// always returns error. ErrServerClosed on graceful close
		if err := srv.ListenAndServe(); err != http.ErrServerClosed {
			// unexpected error. port in use?
			log.Fatalf("ListenAndServe(): %v", err)
		}
	}()
	// returning reference so caller can call Shutdown()
	return srv
}

func (v *VisitLogController) serveLog(w http.ResponseWriter, r *http.Request) {
	decoder := json.NewDecoder(r.Body)
	var params postBody
	decoder.Decode(&params)
	v.visitLogService.InitService()
	defer v.visitLogService.Destr()
	v.visitLogService.Log(params.ShortenerID,params.IP,params.Device,params.OwnerID,params.ShortenID)
}