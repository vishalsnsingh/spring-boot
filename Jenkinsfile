
pipeline {
    // Run this pipeline on any available Jenkins agent
    agent any

    stages {

        // Stage 1: Checkout source code from GitHub
        stage('Checkout') {
            steps {
                // Clone the GitHub repository into the Jenkins workspace
                git 'https://github.com/vishalsnsingh/spring-boot.git'
            }
        }

        // Stage 2: Build the Spring Boot application
        stage('Build') {
            steps {
                // Give execute permission to Maven Wrapper
                sh 'chmod +x mvnw'

                // Clean previous build files and package the application
                sh './mvnw --batch-mode clean package'
            }
        }

        // Stage 3: Run automated tests
        stage('Test') {
            steps {
                // Run all Maven tests
                sh './mvnw --batch-mode test'
            }

            post {
                // Publish test results even if the test stage fails
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        // Stage 4: Deploy the application
        stage('Deploy') {
            steps {
                // Placeholder command; replace with actual deployment steps
                sh 'echo "Application deployed successfully"'
            }
        }
    }

    // Actions to execute after the entire pipeline finishes
    post {
        success {
            // Runs when all stages complete successfully
            echo 'Pipeline completed successfully!'
        }

        failure {
            // Runs if any stage fails
            echo 'Pipeline failed! Check the Jenkins console output.'
        }

        always {
            // Runs regardless of pipeline result
            echo 'Pipeline execution finished.'
        }
    }
}
