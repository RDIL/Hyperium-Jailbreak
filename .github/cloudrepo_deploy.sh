#!/bin/bash

USER_ID=deploy@rdil.rocks

ORGANIZATION_ID=hyperiumjailbreak
if [[ -z ${CIRRUS_TAG} ]]; then
  echo "Error: no tag specified. Use export CIRRUS_TAG=theTagIdHere"
  exit 1
fi

if [[ -z ${CR_DEPLOY_TOKEN} ]]; then
  echo "Error, it seems you don't have the deploy token set. Set it first by running export CR_DEPLOY_TOKEN=thePasswordHere"
  exit 1
fi

if [[ ${CIRRUS_TAG} == *"dev"* ]]; then
  # dev build, need to upload to devpack repo
  REPOSITORY_ID="devpacks"
else
  REPOSITORY_ID="releases"
fi

ENCODED_PW=`echo -n ${USER_ID}:${CR_DEPLOY_TOKEN} | base64 -`
FILE_TO_UPLOAD=build/libs/*.jar

TARGET_URL=https://${ORGANIZATION_ID}.mycloudrepo.io/repositories/${REPOSITORY_ID}/

HTTP_STATUS=`curl -s -w "%{http_code}" -X PUT ${TARGET_URL} -H "Authorization: Basic ${ENCODED_PW}" -d @${FILE_TO_UPLOAD}`

if [[ ${HTTP_STATUS} -eq 200 ]]; then
  echo Jar successfully uploaded to CloudRepo and can be retrieved from [${TARGET_URL}]!
else
  echo Failed to upload Jar file to CloudRepo [${TARGET_URL}] Result: $HTTP_STATUS
  exit 1
fi

FILE_TO_UPLOAD=build/libs/*.sha512

HTTP_STATUS=`curl -s -w "%{http_code}" -X PUT ${TARGET_URL} -H "Authorization: Basic ${ENCODED_PW}" -d @${FILE_TO_UPLOAD}`

if [[ ${HTTP_STATUS} -eq 200 ]]; then
  echo SHA File Successfully uploaded to CloudRepo and can be retrieved from [${TARGET_URL}]!
else
  echo Failed to upload SHA file to CloudRepo [${TARGET_URL}] Result: $HTTP_STATUS
  exit 1
fi

