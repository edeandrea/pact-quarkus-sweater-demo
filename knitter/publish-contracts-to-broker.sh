#! /bin/bash
set -x

pact_consumer_version=${1:-1.0}

./mvnw pact:publish -Dpact.publish.consumer.version=${pact_consumer_version}
