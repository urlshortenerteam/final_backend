package dao
import(
	"github.com/violedo/logService/entity"
)
//DAO log record interface
type DAO interface{
	InitDB() error
	Destr()
	InsertLog(entity.Visit) error
	UpdateUser(ID uint64) error
	UpdateShorten(ID uint64) error
	ByShortURL(shortURL string) (shortenID uint64, shortenerID string, err error)
}
