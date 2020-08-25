package entity
// ShortURL entity for a full record of short URL
type ShortURL struct{
	ID int `json:"id"`
	Short string `json:"shortUrl"`
	LongURLs []MongoShort
}

// MongoShort structure for MongoDB
type MongoShort struct {
	ShortenID int64  `bson:"shortenId" json:"shortenId"`
	LongID string `json:"longId" bson:"_id,omitempty"`
	LongURL   string `bson:"longUrl" json:"longUrl"`
}
