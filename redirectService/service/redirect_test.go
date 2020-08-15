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
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockDAO := mock_dao.NewMockIShortUrl(mockCtrl)
	gomock.InOrder(
		mockDAO.EXPECT().InitShortDAO(),
		mockDAO.EXPECT().ByShortURL("5lJ4Vc").Return(entity.ShortUrl{1, "5lJ4Vc", []string{"https://www.baidu.com/", "BANNED", "LIFT"}}, nil),
		mockDAO.EXPECT().ByShortURL("sdIMYI").Return(entity.ShortUrl{2, "sdIMYI", []string{"https://www.baidu.com/", "BANNED"}}, nil),
		mockDAO.EXPECT().ByShortURL("$$$$$$").Return(entity.ShortUrl{1, "5lJ4Vc", []string{"https://www.baidu.com/"}}, sql.ErrNoRows),
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
			if gotLongURL := tt.r.ShortToLong(tt.args.shortURL); gotLongURL != tt.wantLongURL {
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
