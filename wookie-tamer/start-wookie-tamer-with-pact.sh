#!/bin/bash

quarkus dev --clean -Dpact.verifier.publishResults=true -Dpact.provider.version=1.0 -Dpact.provider.branch=wookie -Dpactbroker.auth.token=$WOOKIES_PACTBROKER_AUTH_TOKEN