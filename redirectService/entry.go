package main

import (
	"sync"
	"github.com/ao7777/redirectService/controller"
	"github.com/ao7777/redirectService/service"
)

func main() {
	wait := &sync.WaitGroup{}
	wait.Add(1)
	control:= &controller.RedirectController{}
	control.Init(wait, &service.Redirect{})
	wait.Add(1)
	wait.Wait()
}
