package controller

import (
	"testing"

	isrv "github.com/violedo/logService/interface/service"
	mockservice "github.com/violedo/logService/interface/service/mocks"
	"github.com/golang/mock/gomock"
)

func TestVisitLogController_Init(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockService := mockservice.NewMockILogService(mockCtrl)
	gomock.InOrder(
		mockService.EXPECT().InitService(),
	)
	type args struct {
		visitLogService isrv.ILogService
	}
	tests := []struct {
		name string
		v    *VisitLogController
		args args
	}{
		{
			"Init test",
			&VisitLogController{},
			args{mockService},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.v.Init(tt.args.visitLogService)
		})
	}
}

func TestVisitLgController_Destr(t *testing.T) {
	mockCtrl := gomock.NewController(t)
	defer mockCtrl.Finish()
	mockService := mockservice.NewMockILogService(mockCtrl)
	gomock.InOrder(
		mockService.EXPECT().InitService(),
		mockService.EXPECT().Destr(),
	)
	tests := []struct {
		name string
		v    *VisitLogController
	}{
		{
			"Init test",
			&VisitLogController{},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.v.Init(mockService)
			tt.v.Destr()
		})
	}
}
