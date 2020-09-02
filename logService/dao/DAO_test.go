package dao

import (
	"testing"
	"time"

	sqlmock "github.com/DATA-DOG/go-sqlmock"
	_ "github.com/go-sql-driver/mysql"
	"github.com/violedo/logService/entity"
)

func TestLogDAO_InitDB(t *testing.T) {

	tests := []struct {
		name    string
		l       *LogDAO
		wantErr bool
	}{
		{
			"Init Test",
			&LogDAO{},
			false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if err := tt.l.InitDB(); (err != nil) != tt.wantErr {
				t.Errorf("LogDAO.InitDB() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestLogDAO_Destr(t *testing.T) {
	tests := []struct {
		name string
		l    *LogDAO
	}{
		{
			"Destroy test",
			&LogDAO{},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.l.InitDB()
			tt.l.Destr()
		})
	}
}

func TestLogDAO_InsertLog(t *testing.T) {
	type args struct {
		v entity.Visit
	}
	time := time.Now()

	db, mock, err := sqlmock.New()
	if err != nil {
		t.Fatalf("an error '%s' was not expected when opening a stub database connection", err)
	}
	defer db.Close()

	mock.ExpectPrepare("INSERT visit_log SET shortener_id=\\?,visit_time=\\?,ip=\\?,device=\\?")
	mock.ExpectExec("INSERT visit_log SET shortener_id=\\?,visit_time=\\?,ip=\\?,device=\\?").
		WithArgs("5f223b84b3f08a6a051c90cc", time, "0.0.0.0", false).WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()
	logdao := &LogDAO{db: db}
	tests := []struct {
		name    string
		l       *LogDAO
		args    args
		wantErr bool
	}{
		{
			"Insert test",
			logdao,
			args{entity.Visit{ShortenerID: "5f223b84b3f08a6a051c90cc",
				VisitTime: time,
				IP:        "0.0.0.0",
				Device:    false},
			},
			false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			//tt.l.InitDB()
			if err := tt.l.InsertLog(tt.args.v); (err != nil) != tt.wantErr {
				t.Errorf("LogDAO.InsertLog() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestLogDAO_UpdateUser(t *testing.T) {
	type args struct {
		ID uint64
	}

	db, mock, err := sqlmock.New()
	if err != nil {
		t.Fatalf("an error '%s' was not expected when opening a stub database connection", err)
	}
	defer db.Close()

	mock.ExpectPrepare("SELECT visit_count FROM users WHERE id = \\?")
	mock.ExpectQuery("SELECT visit_count FROM users WHERE id = \\?").WithArgs(1).WillReturnRows(mock.NewRows([]string{"visit_count"}).AddRow(1))

	mock.ExpectPrepare("update users set visit_count=\\? where id=\\?")
	mock.ExpectExec("update users set visit_count=\\? where id=\\?").
		WithArgs(2, 1).WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()
	logdao := &LogDAO{db: db}

	tests := []struct {
		name    string
		l       *LogDAO
		args    args
		wantErr bool
	}{
		{
			"Successful Update User test",
			logdao,
			args{1},
			false,
		},
		{
			"Wrong Update User test",
			logdao,
			args{0},
			true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			//tt.l.InitDB()
			if err := tt.l.UpdateUser(tt.args.ID); (err != nil) != tt.wantErr {
				t.Errorf("LogDAO.UpdateUser() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestLogDAO_UpdateShorten(t *testing.T) {
	type args struct {
		ID int64
	}

	db, mock, err := sqlmock.New()
	if err != nil {
		t.Fatalf("an error '%s' was not expected when opening a stub database connection", err)
	}
	defer db.Close()

	mock.ExpectPrepare("SELECT visit_count FROM shorten_log WHERE id = \\?")
	mock.ExpectQuery("SELECT visit_count FROM shorten_log WHERE id = \\?").WithArgs(1).WillReturnRows(mock.NewRows([]string{"visit_count"}).AddRow(1))

	mock.ExpectPrepare("update shorten_log set visit_count=\\? where id=\\?")
	mock.ExpectExec("update shorten_log set visit_count=\\? where id=\\?").
		WithArgs(2, 1).WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()
	logdao := &LogDAO{db: db}

	tests := []struct {
		name    string
		l       *LogDAO
		args    args
		wantErr bool
	}{
		{
			"Successful update shortenLog test",
			logdao,
			args{1},
			false,
		},
		{
			"Wrong update shortenLog test",
			&LogDAO{},
			args{-1},
			true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.l.InitDB()
			if err := tt.l.UpdateShorten(tt.args.ID); (err != nil) != tt.wantErr {
				t.Errorf("LogDAO.UpdateShorten() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestLogDAO_ByShortenID(t *testing.T) {
	type args struct {
		shortenID int64
	}
	tests := []struct {
		name      string
		l         LogDAO
		args      args
		wantOwner uint64
		wantErr   bool
	}{
		{
			"successful test",
			LogDAO{},
			args{1},
			1,
			false,
		},
		{
			"successful test",
			LogDAO{},
			args{0},
			0,
			true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.l.InitDB()
			gotOwner, err := tt.l.ByShortenID(tt.args.shortenID)
			if (err != nil) != tt.wantErr {
				t.Errorf("LogDAO.ByShortenID() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if gotOwner != tt.wantOwner {
				t.Errorf("LogDAO.ByShortenID() = %v, want %v", gotOwner, tt.wantOwner)
			}
		})
	}
}
