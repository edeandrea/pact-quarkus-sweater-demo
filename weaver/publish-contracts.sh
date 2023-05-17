#! /bin/sh
set -x

rm -rf ../wookie-tamer/src/test/resources/pacts
mkdir -p ../wookie-tamer/src/test/resources/pacts
cp ./target/pacts/* ../wookie-tamer/src/test/resources/pacts
