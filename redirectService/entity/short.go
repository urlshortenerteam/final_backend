package entity

type ShortUrl struct{
	ID int `json:"id"`
	Short string `json:"shortUrl"`
	LongUrl []string `json:"longUrl"`
}