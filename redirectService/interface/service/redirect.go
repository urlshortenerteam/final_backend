package service
import "github.com/ao7777/redirectService/interface/dao"
//IRedirect Redirect service interface
type IRedirect interface{
	Init(shortDAO ...dao.IShortUrl)
	Destr()
	ShortToLong(shortURL string) (longURL string)
}