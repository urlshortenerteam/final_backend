package main

import (
	"github.com/violedo/logService/controller"
	"github.com/violedo/logService/service"
	// "sync"
)

func main() {
	// wait := &sync.WaitGroup{}
	// wait.Add(1)
	control := &controller.VisitLogController{}
	control.Init(&service.VisitService{})
	control.ServeLog()
	control.Destr()
	// wait.Add(1)
	// wait.Wait()
}
