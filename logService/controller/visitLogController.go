package controller

import (
	// "net/http"
	// "sync"

	log "github.com/sirupsen/logrus"
	isrv "github.com/violedo/logService/interface/service"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"encoding/json"
	"github.com/joho/godotenv"
	"os"
	"os/signal"
	"syscall"
	// "regexp"
	"strings"
)

//VisitLogController the visitLog controller
type VisitLogController struct {
	visitLogService isrv.ILogService
	consumer *kafka.Consumer
}

// type postBody struct {
// 	Device      bool   `json:"device"`
// 	OwnerID     uint64 `json:"ownerId"`
// 	ShortenID   uint64 `json:"shortenId"`
// 	IP          string `json:"ip"`
// 	ShortenerID string `json:"shortenerId"`
// }

func init() {
	if err := godotenv.Load(os.Getenv("TEST_DIR") + "credentials.env"); err != nil {
		log.Fatal(err)
	}
}

//Init initialization of the controller
func (v *VisitLogController) Init(visitLogService isrv.ILogService) {
	// srv := &http.Server{Addr: ":9092"}
	v.visitLogService = visitLogService
	v.visitLogService.InitService()

	var err error
	v.consumer,err = kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": os.Getenv("BOOTSTRAP_SERVERS"),
		"group.id":          os.Getenv("KAFKA_GROUP"),
		"auto.offset.reset": "earliest"})
	if err != nil {
		log.Fatal(err)
	}
	topic :=[]string{os.Getenv("KAFKA_TOPIC")}
	err = v.consumer.SubscribeTopics(topic, nil)

}

//ServeLog gets message from kafka and handle it
func (v *VisitLogController) ServeLog() {
	// decoder := json.NewDecoder(r.Body)
	// var params postBody
	// decoder.Decode(&params)
	// v.visitLogService.Log(params.ShortenerID,params.IP,params.Device,params.OwnerID,params.ShortenID)

	// regPath := regexp.MustCompile(`[A-Za-z0-9]{6}`)
	// log.Info(r.URL.Path[1:])
	// if regPath.MatchString(r.URL.Path[1:]) == false {
	// 	return
	// }

	type logMessage struct{
		ShortenID uint64 `json:"shortenId`
		LongID string `json:"longId"`
		LongURL string `json:"longUrl"`
		IP string `json:"ip"`
		ShortURL string `json:"shortUrl"`
		UserAgent string `json:"User-Agent"`
	}
	var message logMessage
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)
	run := true
	for run == true {
		select {
		case sig := <-sigchan:
			log.WithFields(log.Fields{
				"Signal": sig,
			}).Info("Caught signal terminating.")
			run = false
		default:
			ev := v.consumer.Poll(100)
			if ev == nil {
				continue
			}

			switch e := ev.(type) {
			case *kafka.Message:
				go func() {
					log.WithFields(log.Fields{
						"topicPartition": e.TopicPartition,
						"messageValue":string(e.Value),
					}).Info("Kafka Message.")
					json.Unmarshal(e.Value,&message)
					var device bool
					if strings.Index(message.UserAgent, "Windows") > -1 || strings.Index(message.UserAgent, "Linux") > -1 || strings.Index(message.UserAgent, "Mac") > -1 {
						device = false
					} else {
						device = true
					}
					v.visitLogService.Log(message.ShortenID,message.LongID, message.IP, device)
				}()

			case kafka.Error:
				// Errors should generally be considered
				// informational, the client will try to
				// automatically recover.
				// But in this example we choose to terminate
				// the application if all brokers are down.
				log.WithFields(log.Fields{
					"code": e.Code(),
					"error": e,
				}).Info("Kafka Error.")
				if e.Code() == kafka.ErrAllBrokersDown {
					run = false
				}
			default:
				log.WithFields(log.Fields{
					"type": e,
				}).Info("Kafka Ignored.")
			}
		}
	}

	log.Info("Close consumer\n")
	v.consumer.Close()

}

//Destr destroys controller
func (v *VisitLogController) Destr(){
	v.visitLogService.Destr()
}
