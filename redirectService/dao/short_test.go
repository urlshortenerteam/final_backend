package dao

import (
	"database/sql"
	"testing"

	idao "github.com/ao7777/redirectService/interface/dao"
)

func TestNoInitByID(t *testing.T) {
	t.Parallel()
	var shortDAO idao.IShortUrl
	shortDAO = &ShortDAO{}
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Ill-handled uninitialized database operation ByID.")
		}
	}()
	_, _ = shortDAO.ByID(1)
}
func TestNoInitByShortURL(t *testing.T) {
	t.Parallel()
	var shortDAO idao.IShortUrl
	shortDAO = &ShortDAO{}
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Ill-handled uninitialized database operation ByShortURL.")
		}
	}()
	_, _ = shortDAO.ByShortURL("$$$$$$")
}
func TestByID(t *testing.T) {
	t.Parallel()
	var shortDAO idao.IShortUrl
	shortDAO = &ShortDAO{}
	err := shortDAO.InitShortDAO()
	if err != nil {
		t.Fatal(err)
	}
	defer shortDAO.Destr()
	e, err := shortDAO.ByID(1)
	if err != nil {
		t.Error(err)
	}
	if e.ID != 1 {
		t.Error("Incorrect entity queried.")
	}
}
func TestByShortURL(t *testing.T) {
	t.Parallel()
	var shortDAO idao.IShortUrl
	shortDAO = &ShortDAO{}
	err := shortDAO.InitShortDAO()
	if err != nil {
		t.Fatal(err)
	}
	defer shortDAO.Destr()
	e, err := shortDAO.ByShortURL("5lJ4Vc")
	if err != nil {
		t.Error(err)
	}
	if e.Short != "5lJ4Vc" {
		t.Error("Incorrect entity queried.")
	}
	e, err = shortDAO.ByShortURL("$$$$$$")
	if err != sql.ErrNoRows || e.LongURLs[0].LongURL != "NOTFOUND" {
		t.Error("Incorrect entity queried.")
	}
}
