package service

import (
	"database/sql"
	"github.com/ao7777/redirectService/interface/dao/mocks"
	"testing"

	"github.com/ao7777/redirectService/entity"
	idao "github.com/ao7777/redirectService/interface/dao"
	"github.com/golang/mock/gomock"
)

func TestRedirect_ShortToLong(t *testing.T) {
	t.Parallel()
	type args struct {
		shortURL string
	}
	var re Redirect
	mongo1,mongo2,mongo3:=[]entity.MongoShort{
		{ShortenID:0,LongID: "",LongURL: "https://www.baidu.com/"},
		{ShortenID:1,LongID: "",LongURL: "BANNED"},
		{ShortenID:2,LongID: "",LongURL: "LIFT"},
	},[]entity.MongoShort{
		{ShortenID:0,LongID: "",LongURL: "https://www.baidu.com/"},
		{ShortenID:1,LongID: "",LongURL: "BANNED"},
	},[]entity.MongoShort{
		{ShortenID:0,LongID: "",LongURL: "https://www.baidu.com/"},
	}
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockDAO := mock_dao.NewMockIShortUrl(mockCtrl)
	gomock.InOrder(
		mockDAO.EXPECT().InitShortDAO(),
		mockDAO.EXPECT().ByShortURL("5lJ4Vc").Return(entity.ShortURL{ID:1, Short: "5lJ4Vc",LongURLs:  mongo1}, nil),
		mockDAO.EXPECT().ByShortURL("sdIMYI").Return(entity.ShortURL{ID:2, Short: "sdIMYI",LongURLs:  mongo2}, nil),
		mockDAO.EXPECT().ByShortURL("$$$$$$").Return(entity.ShortURL{ID:1, Short: "5lJ4Vc",LongURLs:  mongo3}, sql.ErrNoRows),
		mockDAO.EXPECT().Destr(),
	)
	re.Init(mockDAO)
	defer re.Destr()
	tests := []struct {
		name        string
		r           *Redirect
		args        args
		wantLongURL string
	}{
		{"NormalJump", &re, args{"5lJ4Vc"}, "https://www.baidu.com/"},
		{"Banned", &re, args{"sdIMYI"}, "BANNED"},
		{"NoneExisting", &re, args{"$$$$$$"}, "NOTFOUND"},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if gotLongURL := tt.r.ShortToLong(tt.args.shortURL); gotLongURL.LongURL != tt.wantLongURL {
				t.Errorf("Redirect.ShortToLong() = %v, want %v", gotLongURL, tt.wantLongURL)
			}
		})
	}
}

func TestRedirect_Init(t *testing.T) {
	re := Redirect{}
	type args struct {
		shortDAO []idao.IShortUrl
	}
	tests := []struct {
		name string
		r    *Redirect
		args args
	}{
		{"Initialize", &re, args{}},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.r.Init(tt.args.shortDAO...)
		})
	}
}
