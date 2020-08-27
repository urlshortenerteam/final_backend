package controller

// import (
// 	"net/http"
// 	"net/http/httptest"

// 	// "reflect"
// 	"strings"
// 	"sync"
// 	"testing"

// 	// isrv "github.com/violedo/logService/interface/service"
// 	"github.com/violedo/logService/service"
// )

// // func TestVisitLogController_Init(t *testing.T) {
// // 	type args struct {
// // 		wg              *sync.WaitGroup
// // 		visitLogService isrv.ILogService
// // 	}
// // 	tests := []struct {
// // 		name string
// // 		v    *VisitLogController
// // 		args args
// // 		want *http.Server
// // 	}{
// // 		{
// // 			"Controller Initialization",
// // 			&VisitLogController{},
// // 			args{&sync.WaitGroup{},&service.VisitService{}},
// // 			&http.Server{Addr: ":9092"},
// // 		},
// // 	}
// // 	for _, tt := range tests {
// // 		t.Run(tt.name, func(t *testing.T) {
// // 			if got := tt.v.Init(tt.args.wg, tt.args.visitLogService); !reflect.DeepEqual(got, tt.want) {
// // 				t.Errorf("VisitLogController.Init() = %v, want %v", got, tt.want)
// // 			}
// // 		})
// // 	}
// // }

// func TestVisitLogController_serveLog(t *testing.T) {
// 	type args struct {
// 		w http.ResponseWriter
// 		r *http.Request
// 	}
// 	v := VisitLogController{}
// 	v.visitLogService = &service.VisitService{}
// 	r1, _ := http.NewRequest(http.MethodGet, "/5lJ4Vc", strings.NewReader(`{"short":"5lJ4Vc"}`))
// 	// r1,_ := http.NewRequest(http.MethodPost, "/visitLog", strings.NewReader("{\"shortenerId\": \"5f223b84b3f08a6a051c90cc\",\"ip\": \"123.159.111.147\",\"device\": false,\"ownerId\": 1,\"shortenId\": 1}"))
// 	// r2,_ := http.NewRequest(http.MethodPost, "/visitLog", strings.NewReader("{\"shortenerId\": \"5f223b84b3f08a6a051c90cc\",\"ip\": \"123.159.111.147\",\"device\": false,\"ownerId\": 0,\"shortenId\": 1}"))
// 	// r3,_ := http.NewRequest(http.MethodPost, "/visitLog", strings.NewReader("{\"shortenerId\": \"5f223b84b3f08a6a051c90cc\",\"ip\": \"123.159.111.147\",\"device\": false,\"ownerId\": 1,\"shortenId\": 0}"))

// 	tests := []struct {
// 		name string
// 		v    *VisitLogController
// 		args args
// 	}{
// 		{
// 			"Successful visit test",
// 			&v,
// 			args{httptest.NewRecorder(), r1},
// 		},
// 		// {
// 		// 	"Wrong ownerID",
// 		// 	&v,
// 		// 	args{httptest.NewRecorder(),r2},
// 		// },
// 		// {
// 		// 	"Wrong shortenID",
// 		// 	&v,
// 		// 	args{httptest.NewRecorder(),r3},
// 		// },
// 	}
// 	for _, tt := range tests {
// 		t.Run(tt.name, func(t *testing.T) {
// 			tt.v.Init(&sync.WaitGroup{}, &service.VisitService{})
// 			tt.v.serveLog(tt.args.w, tt.args.r)
// 		})
// 	}
// }
