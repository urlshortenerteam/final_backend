package service
import "github.com/violedo/logService/interface/dao"
//ILogService visitlog insert service interface
type ILogService interface{
	InitService(logDAO ...dao.DAO)
	Destr()
	Log(shortenID uint64, longID string, IP string, Device bool)
}