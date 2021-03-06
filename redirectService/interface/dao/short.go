package dao
import(
	"github.com/ao7777/redirectService/entity"
)

type IShortUrl interface{
	ByID(id int) (e entity.ShortUrl,err error)
	InitShortDAO() error
	Destr()
	ByShortURL(shortUrl string) (e entity.ShortUrl, err error)
}
