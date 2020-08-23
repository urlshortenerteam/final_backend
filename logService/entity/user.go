package entity
//User the entity of users
type User struct{
	ID uint64 `json:"id"`
	VisitCount uint64 `json:"visitCount"`
}