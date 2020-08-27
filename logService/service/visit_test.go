package service

import (
	"testing"

	idao "github.com/violedo/logService/interface/dao"
)

func TestVisitService_Log(t *testing.T) {
	type args struct {
		shortenID uint64
		longID    string
		IP        string
		Device    bool
	}
	v := VisitService{}
	v.InitService()
	defer v.Destr()
	tests := []struct {
		name string
		v    VisitService
		args args
	}{
		{
			"success test",
			v,
			args{1, "5f223b84b3f08a6a051c90cc", "0.0.0.0", false},
		},
		{
			"wrong shortenID",
			v,
			args{0, "5f223b84b3f08a6a051c90cc", "0.0.0.0", false},
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
			tt.v.InitService()
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
