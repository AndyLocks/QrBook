#!/bin/bash

cat ./proto/services.proto > ./auth_service/src/main/resources/services.proto
cat ./proto/services.proto > ./codes_handler/src/main/resources/services.proto
cat ./proto/services.proto > ./account_service/src/main/resources/services.proto
cat ./proto/services.proto > ./redirect_service/src/main/resources/services.proto
