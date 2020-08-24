package service
import "github.com/violedo/logService/interface/dao"
//ILogService visitlog insert service interface
type ILogService interface{
	InitService(logDAO ...dao.DAO)
	Destr()
	Log(shortURL string, IP string, Device bool)
}