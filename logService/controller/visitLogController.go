package controller

import (
	"net/http"
	"sync"

	log "github.com/sirupsen/logrus"
	isrv "github.com/violedo/logService/interface/service"

	//"encoding/json"
	"regexp"
	"strings"
)

//VisitLogController the visitLog controller
type VisitLogController struct {
	visitLogService isrv.ILogService
}

type postBody struct {
	Device      bool   `json:"device"`
	OwnerID     uint64 `json:"ownerId"`
	ShortenID   uint64 `json:"shortenId"`
	IP          string `json:"ip"`
	ShortenerID string `json:"shortenerId"`
}

//Init initialization of the controller
func (v *VisitLogController) Init(wg *sync.WaitGroup, visitLogService isrv.ILogService) *http.Server {
	srv := &http.Server{Addr: ":9092"}
	v.visitLogService = visitLogService
	v.visitLogService.InitService()
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
	// decoder := json.NewDecoder(r.Body)
	// var params postBody
	// decoder.Decode(&params)
	// v.visitLogService.Log(params.ShortenerID,params.IP,params.Device,params.OwnerID,params.ShortenID)
	regPath := regexp.MustCompile(`[A-Za-z0-9]{6}`)
	log.Info(r.URL.Path[1:])
	if regPath.MatchString(r.URL.Path[1:]) == false {
		return
	}

	var ip string
	forwarded := r.Header.Get("X-FORWARDED-FOR")
	if forwarded != "" {
		ip = forwarded
	}
	ip = r.RemoteAddr
	log.Info(ip)
	userAgent := r.UserAgent()
	var device bool
	if strings.Index(userAgent, "Windows") > -1 || strings.Index(userAgent, "Linux") > -1 || strings.Index(userAgent, "Mac") > -1 {
		device = false
	} else {
		device = true
	}
	v.visitLogService.Log(r.URL.Path[1:], ip, device)
}
