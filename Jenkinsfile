
pipeline {
    agent any

    tools {
        jdk 'JDK 21'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Building Spring Boot application and running tests...'

                sh 'chmod +x mvnw'

                sh './mvnw --batch-mode clean verify'
            }

            post {
                always {
                    echo 'Publishing test results...'

                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging Spring Boot application...'

                sh './mvnw --batch-mode package -DskipTests'
            }
        }

    }

    post {

        success {
            echo 'Build completed successfully!'

            archiveArtifacts artifacts: 'target/*.jar',
                             fingerprint: true
        }

        failure {
            echo 'Build failed! Please check the Jenkins console output.'
        }

        always {
            echo 'Pipeline execution completed.'
        }
    }
}
