pipeline {
    agent any
    tools { jdk 'JDK17'; maven 'Maven3' }
    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'edge', 'firefox'], description: 'UI browser')
        choice(name: 'ENVIRONMENT', choices: ['qa', 'staging', 'prod'], description: 'Target environment')
    }
    stages {
        stage('Clean') { steps { sh 'mvn -B clean' } }
        stage('Build and Test') {
            steps { sh "mvn -B verify -Denv=${params.ENVIRONMENT} -Dbrowser=${params.BROWSER} -Dheadless=true" }
        }
    }
    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'target/extent-reports/**,target/logs/**', allowEmptyArchive: true
        }
    }
}
