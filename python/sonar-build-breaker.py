#!/usr/bin/env python3

import sys
import requests
import json
import urllib
import time

# Configuraciones
quality_gate_path = '/api/qualitygates/project_status'
last_anaylisis_path = '/api/project_analyses/search'
sleep_time = 5

args = sys.argv

args.pop(0)

if len(args) != 4:
    print("Usage: sonar-build-breaker URL TOKEN PROJECT_KEY FROM_DATETIME")
    exit(1)

host, token, project_key, from_datetime = args

analysis_full_url = host + last_anaylisis_path + '?project=' + project_key + '&from=' + urllib.parse.quote(from_datetime)

while True:
    analysis_query_str = requests.get(analysis_full_url, auth=(token, ""))

    if analysis_query_str.status_code != 200:
        print('Error: ' + analysis_query_str.text)
        exit(2)

    response: object = json.loads(analysis_query_str.text)

    if len(response['analyses']) > 0:
        analysis_id = response['analyses'][0]['key']
        break

    time.sleep(sleep_time)


quality_gate_full_url = host + quality_gate_path + '?analysisId=' + analysis_id

quality_gate_query = requests.get(quality_gate_full_url, auth=(token, ""))

print(quality_gate_query.text)
response = json.loads(quality_gate_query.text)

report = response['projectStatus']['conditions']

for c in report:
    metric = c['metricKey']
    umbral = c['errorThreshold']
    sentido = 'menor' if c['comparator'] == 'LT' else 'mayor'
    valor = c['actualValue']
    status = c['status']

    s = metric + ' [' + status + ']: ' + valor + ' es ' + sentido + ' que ' + umbral

    print(s)

exit_status = 0

if response['projectStatus']['status'] == 'ERROR':
    print('No se pasó el umbral de calidad')
    exit_status += 100


exit(exit_status)
