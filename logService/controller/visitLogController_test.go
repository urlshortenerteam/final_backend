package controller

import (
	"testing"

	isrv "github.com/violedo/logService/interface/service"
	"github.com/violedo/logService/service"
)

func TestVisitLogController_Init(t *testing.T) {
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
			args{&service.VisitService{}},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.v.Init(tt.args.visitLogService)
		})
	}
}

func TestVisitLgController_Destr(t *testing.T) {
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
			tt.v.Init(&service.VisitService{})
			tt.v.Destr()
		})
	}
}
