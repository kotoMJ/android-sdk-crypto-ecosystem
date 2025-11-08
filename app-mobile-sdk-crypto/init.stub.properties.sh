mkdir properties
cat <<EOF >properties/app.content.properties
    sdk.iam.auth0.clientId="SAMPLE_AUTH0_CLIENT_ID"
    sdk.recommendation.prod.apikey="SAMPLE_RECOMMENDATION_PRODUCTION_API_KEY"
    sdk.recommendation.dev.apikey="SAMPLE_RECOMMENDATION_DEVELOPMENT_API_KEY"
EOF
