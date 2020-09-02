package service

import (
	"testing"

	idao "github.com/violedo/logService/interface/dao"
	"github.com/golang/mock/gomock"
	mockdao "github.com/violedo/logService/interface/dao/mocks"
)

func TestVisitService_Log(t *testing.T) {
	type args struct {
		shortenID int64
		longID    string
		IP        string
		Device    bool
	}

	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockDao := mockdao.NewMockDAO(mockCtrl)
	gomock.InOrder(
		mockDao.EXPECT().InitDB(),
		mockDao.EXPECT().ByShortenID(int64(1)).Return(uint64(1), nil),
		mockDao.EXPECT().InsertLog(gomock.Any()).Return(nil),
		mockDao.EXPECT().UpdateUser(uint64(1)).Return(nil),
		mockDao.EXPECT().UpdateShorten(int64(1)).Return(nil),
		mockDao.EXPECT().Destr(),
	)

	v := VisitService{}
	v.InitService(mockDao)
	defer v.Destr()

	tests := []struct {
		name string
		v    VisitService
		args args
	}{
		{
			"log test",
			v,
			args{1, "5f223b84b3f08a6a051c90cc", "0.0.0.0", false},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.v.Log(tt.args.shortenID, tt.args.longID, tt.args.IP, tt.args.Device)
		})
	}
}

func TestVisitService_InitService(t *testing.T) {
	type args struct {
		logDAO []idao.DAO
	}
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockDao := mockdao.NewMockDAO(mockCtrl)
	gomock.InOrder(
		mockDao.EXPECT().InitDB(),
	)

	tests := []struct {
		name string
		v    *VisitService
		args args
	}{
		{
			"Init test",
			&VisitService{},
			args{[]idao.DAO{}},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.v.InitService(mockDao)
		})
	}
}

func TestVisitService_Destr(t *testing.T) {
	tests := []struct {
		name string
		v    *VisitService
	}{
		{
			"Destr test",
			&VisitService{},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.v.InitService()
			tt.v.Destr()
		})
	}
}
