package utils

import (
	"fmt"
	"math"
	"time"
)

func GenerateProductID() string {
	now := time.Now()
	timestamp := now.Format("20060102-150405") // YYYYMMDD- HHMMSS
	
	// convert nanoseconds to milliseconds and % 100 to keep in 0-999 range
    // 1 millisecond = 1,000,000 nanoseconds
	milliseconds := now.Nanosecond() / int(math.Pow(100, 3))  % 1000  
	return fmt.Sprintf("PROD-%s-%03d", timestamp, milliseconds)
}

func GenerateShortProductID() string {
	return fmt.Sprintf("P-%d", time.Now().UnixMilli())
}
