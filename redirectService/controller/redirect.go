package controller

import (
	"net/http"
	"regexp"
	"sync"

	isrv "github.com/ao7777/redirectService/interface/service"
	log "github.com/sirupsen/logrus"
)

// RedirectController redirect controller structure
type RedirectController struct {
	shortService isrv.IRedirect
}

const hostURL string = "http://rv-s.cn"

func (re *RedirectController) getLong(url string) (longURL string) {
	longURL = re.shortService.ShortToLong(url)
	distURL, ban, notFound := longURL, hostURL+"/static/banned.html", hostURL+"/static/error.html"
	if longURL != "BANNED" && longURL != "NOTFOUND" {
		longURL = distURL
	} else {
		if longURL != "NOTFOUND" {
			longURL = ban
		} else {
			longURL = notFound
		}
	}
	
	return longURL
}

func (re *RedirectController) serveShort(w http.ResponseWriter, r *http.Request) {
	regPath:=regexp.MustCompile(`[A-Za-z0-9]{6}`)
	log.Info(r.URL.Path[1:])
	if regPath.MatchString(r.URL.Path[1:]) == false{
		http.Redirect(w,r,hostURL+"/static/error.html",http.StatusFound)
		return 
	}
	re.shortService.Init()
	defer re.shortService.Destr()
	longURL := re.getLong(r.URL.Path[1:])
	log.WithField("longUrl", longURL).Info("Redirect to distination")
	http.Redirect(w, r, longURL, http.StatusFound)
}

// Init Initialize redirect controller
func (re *RedirectController) Init(wg *sync.WaitGroup, shortService isrv.IRedirect) *http.Server {
	srv := &http.Server{Addr: ":9090"}
	re.shortService = shortService
	http.HandleFunc("/", re.serveShort) // 设置访问的路由
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
