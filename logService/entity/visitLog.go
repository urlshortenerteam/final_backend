package entity
import (
	"time"
)
//Visit the entity of visitLog
type Visit struct{
	ID uint64 `json:"id"`
	ShortenerID string `json:"shortenerId"`
	VisitTime time.Time `json:"visitTime"`
	IP string `json:"ip"`
	Device bool `json:"deviee"`
}