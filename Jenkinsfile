pipeline {
	agent any

	 tools {
        maven 'maven3.9'
        
    }

	environment {
        DOCKER_REPO = "yym-calculator-demo"
        APP_JAR = "target\\Calculator-v1.jar"
        DOCKER_CREDENTIALS_ID = "dockerhub-credentials"
        DOCKER_HOST_PORT = "7070"
    }
	
	stages {
		stage('Checkout'){
			steps {
				git branch: 'main', url: 'https://github.com/Ellin2024/Calculator-Test.git'
			}
		}
		
		stage('Unit Test'){
			steps{
				sh 'mvn test'	
			}
			 post {
                always {
                    junit 'target/surefire-reports/*.xml'
					// jacoco execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java', inclusionPattern: '**/*.class'
                }
            }
		}

		// stage('JaCoCo Report') {
  //           steps {
  //               sh 'mvn jacoco:report'
  //           }
  //       }

		stage('JaCoCo Report') {
            steps {
                // Publish JaCoCo HTML report in Jenkins
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage'
                ])
            }
        }
        stage("Static Code Analysis (Checkstyle)") {
            steps {
                sh "mvn checkstyle:checkstyle"
                publishHTML(target: [
                    reportDir: 'target/site',
                    reportFiles: 'checkstyle.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }
		
		stage('Build Jar'){
			steps{
				 sh 'mvn clean package -DskipTests'
			}
		}
		
		stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image and tag it with build number
                    def imageTag = "${env.BUILD_NUMBER}"
                    sh "docker build -t ${DOCKER_REPO}:${imageTag} ."
                    sh "docker tag ${DOCKER_REPO}:${imageTag} ${DOCKER_REPO}:v1"
                    env.IMAGE_TAG = imageTag
                }
            }
        }
        
        stage('Run Docker Container') {
        steps {
            echo 'Running container locally (port 7070)...'
            sh '''
                docker stop calculator-test || true
                docker rm calculator-test || true
                docker run -d --name yym-calculator-demo -p 7070:8080 yym-calculator-demo-:v1
            '''
        }    
    }
	}
	 post {
          always {
              echo "✅ Pipeline finished."
          }
          success {
             echo "Pipeline succeeded! App running at http://localhost:${env.DOCKER_HOST_PORT}/"
          }
          failure {
              echo "Pipeline failed."
          }
      }
}
