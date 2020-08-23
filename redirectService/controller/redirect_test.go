package controller

import (
	"context"
	"net/http"
	"net/http/httptest"
	"strings"
	"sync"
	"testing"
	"time"
	mockservice "github.com/ao7777/redirectService/interface/service/mocks"
	"github.com/golang/mock/gomock"
)

func Test_serveShort(t *testing.T) {
	t.Parallel()
	mux := http.NewServeMux()
	reader := strings.NewReader(`{"short":"5lJ4Vc"}`)
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockService := mockservice.NewMockIRedirect(mockCtrl)
	gomock.InOrder(
		mockService.EXPECT().Init(),
		mockService.EXPECT().ShortToLong("5lJ4Vc").Return("https://www.baidu.com/"),
		mockService.EXPECT().Destr(),
		mockService.EXPECT().Init(),
		mockService.EXPECT().ShortToLong("sdIMYI").Return("BANNED"),
		mockService.EXPECT().Destr(),
	)
	re := RedirectController{mockService}
	mux.HandleFunc("/", re.serveShort)
	type args struct {
		shortURL string
	}
	tests := []struct {
		name        string
		args        args
		wantLongURL string
		wantStatus  int
	}{
		{"Normal", args{"5lJ4Vc"}, "https://www.baidu.com/", http.StatusFound},
		{"Banned", args{"sdIMYI"}, hostURL + "/static/banned.html", http.StatusFound},
		{"NoneExisting", args{"$$$$$$"}, hostURL + "/static/error.html", http.StatusFound},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			r, _ := http.NewRequest(http.MethodGet, "/"+tt.args.shortURL, reader)
			w := httptest.NewRecorder()
			mux.ServeHTTP(w, r)
			resp := w.Result()
			if resp.StatusCode != http.StatusFound {
				t.Errorf("Response Code: %v", resp.StatusCode)
			}
			if gotStatusCode := resp.StatusCode; gotStatusCode != tt.wantStatus {
				t.Errorf("serveShort() StatusCode = %v, want %v", gotStatusCode, tt.wantStatus)
			}
			gotURL, err := resp.Location()
			if gotURL.String() != tt.wantLongURL {
				t.Errorf("serveShort() Location = %v, want %v", gotURL, tt.wantLongURL)
			}
			if err != nil {
				t.Error(err)
			}
		})
	}

}

func TestInit(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockService := mockservice.NewMockIRedirect(mockCtrl)
	tests := []struct {
		name string
	}{
		{"Initialize"},
	}
	re:=RedirectController{}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			httpServerExitDone := &sync.WaitGroup{}
			httpServerExitDone.Add(1)
			srv := re.Init(httpServerExitDone,mockService)
			time.Sleep(500 * time.Microsecond)
			if err := srv.Shutdown(context.Background()); err != nil {
				t.Fatal(err)
			}
			httpServerExitDone.Wait()
		})
	}
}
