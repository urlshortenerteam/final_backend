package controller

import (
	"encoding/json"
	"net/http"
	"os"
	"regexp"
	"sync"

	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/ao7777/redirectService/entity"
	isrv "github.com/ao7777/redirectService/interface/service"

	"github.com/joho/godotenv"
	log "github.com/sirupsen/logrus"
)

// RedirectController redirect controller structure
type RedirectController struct {
	shortService isrv.IRedirect
	producer *kafka.Producer
}

const hostURL string = "http://rv-s.cn"

var kafkaConfig kafka.ConfigMap

func init() {
	if err := godotenv.Load(os.Getenv("TEST_DIR") + "credentials.env"); err != nil {
		log.Fatal(err)
	}
}

func (re *RedirectController) getLong(url string) (result entity.MongoShort) {
	result =re.shortService.ShortToLong(url)
	distURL, ban, notFound := result.LongURL, hostURL+"/static/banned.html", hostURL+"/static/error.html"
	if result.LongURL != "BANNED" && result.LongURL != "NOTFOUND" {
		result.LongURL = distURL
	} else {
		if result.LongURL != "NOTFOUND" {
			result.LongURL = ban
		} else {
			result.LongURL = notFound
		}
	}
	return result
}

func (re *RedirectController) serveShort(w http.ResponseWriter, r *http.Request) {
	regPath := regexp.MustCompile(`[A-Za-z0-9]{6}`)
	log.Info(r.URL.Path[1:])
	if regPath.MatchString(r.URL.Path[1:]) == false {
		http.Redirect(w, r, hostURL+"/static/error.html", http.StatusFound)
		return
	}
	longURL := re.getLong(r.URL.Path[1:])
	log.WithField("longUrl", longURL).Info("Redirect to distination")
	ua:=r.Header.Get("User-Agent")
	type logMessage struct{
		entity.MongoShort
		IP string `json:"ip"`
		ShortURL string `json:"shortUrl"`
		device string
	}
	message,_:=json.Marshal(logMessage{longURL,GetIP(r),r.URL.Path[1:],ua})
	topic:="visitLog"
	kMessage:=kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic,Partition:kafka.PartitionAny},
		Value: message,
	}
	re.producer.Produce(&kMessage,nil)
	http.Redirect(w, r, longURL.LongURL, http.StatusFound)
}

// Init Initialize redirect controller
func (re *RedirectController) Init(wg *sync.WaitGroup, shortService isrv.IRedirect) *http.Server {
	re.shortService = shortService
	re.shortService.Init()
	var err error
	re.producer, err = kafka.NewProducer(&kafka.ConfigMap{
		"bootstrap.servers": os.Getenv("BOOTSTRAP_SERVERS"),
		"group.id":          os.Getenv("KAFKA_GROUP"),
		"auto.offset.reset": "earliest",
	})
	if err != nil {
		log.Fatal(err)
	}
	srv := &http.Server{Addr: ":9090"}
	http.HandleFunc("/", re.serveShort) // 设置访问的路由
	go func() {
		defer wg.Done() // let main know we are done cleaning up
		defer re.shortService.Destr()
		defer re.producer.Close()
		// always returns error. ErrServerClosed on graceful close
		if err := srv.ListenAndServe(); err != http.ErrServerClosed {
			// unexpected error. port in use?
			log.Fatalf("ListenAndServe(): %v", err)
		}
	}()

	go func(){
		for e:=range re.producer.Events() {
			switch ev:= e.(type) {
				case *kafka.Message:
					if ev.TopicPartition.Error != nil {
						log.Errorf("Delivery failed: %v\n",ev.TopicPartition)
					}else{
						log.Infof("Delivered message to %v\n",ev.TopicPartition)
					}
			}
		}
	}()

	// returning reference so caller can call Shutdown()
	return srv
}

//GetIP get ip from http request
func GetIP(r *http.Request) string {
	forwarded := r.Header.Get("X-FORWARDED-FOR")
	if forwarded != "" {
		return forwarded
	}
	return r.RemoteAddr
}
