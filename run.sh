#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# Step 1: Start the Python LLM Server
echo -e "${GREEN}🚀 Starting Local LLM Server (Python)...${NC}"
cd llm-server || { echo "❌ llm-server directory not found."; exit 1; }

# Check for venv
if [ ! -d "venv" ]; then
    echo -e "${GREEN}📦 Creating Python virtual environment...${NC}"
    python3 -m venv venv
    source venv/bin/activate
    echo -e "${GREEN}📥 Installing Python dependencies...${NC}"
    pip install -r requirements.txt
else
    source venv/bin/activate
fi

# Run Flask LLM API in the background
python explain_server.py &
PYTHON_PID=$!
cd ..

# Step 2: Start Spring Boot App
echo -e "${GREEN}🟢 Starting Spring Boot Application...${NC}"
mvn spring-boot:run

# Cleanup: Stop Python server when Spring Boot app exits
echo -e "${GREEN}🧹 Cleaning up LLM server...${NC}"
kill $PYTHON_PID
