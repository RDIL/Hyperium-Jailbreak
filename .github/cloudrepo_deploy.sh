#!/bin/bash

USER_ID=chris@example.com
PASSPHRASE=changeme

ORGANIZATION_ID=hyperiumjailbreak
REPOSITORY_ID=example-repo

ENCODED_PW=`echo -n ${USER_ID}:${PASSPHRASE} | base64 -`
FILE_TO_UPLOAD=build/libs/*.jar

TARGET_URL=https://${ORGANIZATION_ID}.mycloudrepo.io/repositories/${REPOSITORY_ID}/

HTTP_STATUS=`curl -s -w "%{http_code}" -X PUT ${TARGET_URL} -H "Authorization: Basic ${ENCODED_PW}" -d @${FILE_TO_UPLOAD}`

if [[ ${HTTP_STATUS} -eq 200 ]]; then
    echo File Successfully uploaded to CloudRepo and can be retrieved from [${TARGET_URL}]!
else
    echo Failed to upload file to CloudRepo [${TARGET_URL}] Result: $HTTP_STATUS
fi
