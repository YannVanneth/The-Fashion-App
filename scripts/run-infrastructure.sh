#!/bin/bash

# Script to run both API Gateway and Service Discovery
# Color codes for better output visibility
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}================================================${NC}"
echo -e "${BLUE}  Starting Infrastructure Services${NC}"
echo -e "${BLUE}================================================${NC}"

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Create logs directory if it doesn't exist
LOGS_DIR="$PROJECT_ROOT/logs"
mkdir -p "$LOGS_DIR"

# Define service paths
SERVICE_DISCOVERY_DIR="$PROJECT_ROOT/infrastructure/service-discovery"
API_GATEWAY_DIR="$PROJECT_ROOT/infrastructure/api-gateway"

# Function to check if a port is in use
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        echo -e "${YELLOW}Warning: Port $port is already in use${NC}"
        return 1
    fi
    return 0
}

# Function to build and run a service
run_service() {
    local service_name=$1
    local service_dir=$2
    local log_file="$LOGS_DIR/${service_name}.log"
    local pid_file="$LOGS_DIR/${service_name}.pid"
    
    echo -e "${GREEN}Starting $service_name...${NC}"
    
    cd "$service_dir" || {
        echo -e "${RED}Error: Cannot access $service_dir${NC}"
        return 1
    }
    
    # Build the service
    echo -e "${BLUE}Building $service_name...${NC}"
    ./gradlew clean build -x test >> "$log_file" 2>&1
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Error: Failed to build $service_name${NC}"
        echo -e "${RED}Check log file: $log_file${NC}"
        return 1
    fi
    
    # Run the service in background
    echo -e "${BLUE}Running $service_name...${NC}"
    nohup ./gradlew bootRun >> "$log_file" 2>&1 &
    local pid=$!
    
    echo -e "${GREEN}$service_name started with PID: $pid${NC}"
    echo -e "${BLUE}Logs: $log_file${NC}"
    echo "$pid" > "$pid_file"
    
    return 0
}

# Start Service Discovery first (Eureka server should start before clients)
echo -e "\n${BLUE}Step 1: Starting Service Discovery (Eureka)${NC}"
run_service "service-discovery" "$SERVICE_DISCOVERY_DIR"

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start Service Discovery. Exiting...${NC}"
    exit 1
fi

# Wait for Service Discovery to be ready
echo -e "${YELLOW}Waiting for Service Discovery to start (30 seconds)...${NC}"
sleep 30

# Start API Gateway
echo -e "\n${BLUE}Step 2: Starting API Gateway${NC}"
run_service "api-gateway" "$API_GATEWAY_DIR"

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start API Gateway.${NC}"
    echo -e "${YELLOW}Service Discovery is still running. Check logs for details.${NC}"
    exit 1
fi

# Wait a bit for API Gateway to start
echo -e "${YELLOW}Waiting for API Gateway to initialize (20 seconds)...${NC}"
sleep 20

echo -e "\n${GREEN}================================================${NC}"
echo -e "${GREEN}  All Infrastructure Services Started!${NC}"
echo -e "${GREEN}================================================${NC}"
echo -e "${BLUE}Service Discovery PID: $(cat $LOGS_DIR/service-discovery.pid 2>/dev/null || echo 'N/A')${NC}"
echo -e "${BLUE}API Gateway PID: $(cat $LOGS_DIR/api-gateway.pid 2>/dev/null || echo 'N/A')${NC}"
echo -e "\n${YELLOW}Logs:${NC}"
echo -e "  - Service Discovery: $LOGS_DIR/service-discovery.log"
echo -e "  - API Gateway: $LOGS_DIR/api-gateway.log"
echo -e "\n${YELLOW}To stop services, run:${NC}"
echo -e "  kill \$(cat $LOGS_DIR/service-discovery.pid) \$(cat $LOGS_DIR/api-gateway.pid)"
echo -e "  ${BLUE}OR${NC}"
echo -e "  pkill -f 'gradle.*bootRun'"
echo -e "${GREEN}================================================${NC}"
