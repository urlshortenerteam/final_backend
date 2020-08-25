package service

import (
	"github.com/ao7777/redirectService/entity"
	"github.com/ao7777/redirectService/interface/dao"
)

//IRedirect Redirect service interface
type IRedirect interface{
	Init(shortDAO ...dao.IShortUrl)
	Destr()
	ShortToLong(shortURL string) (longURL entity.MongoShort)
}