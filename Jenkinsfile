pipeline {
	agent any

	 tools {
        maven 'maven3.9'
        
    }

	environment {
        IMAGE_NAME = "yym-calcu-image"
		CONTAINER_NAME = "yym-calcu-container"
		BUILD_TAG_VERSION = "${BUILD_NUMBER}"
        APP_JAR = "target\\Calculator-v1.jar"
        DOCKER_CREDENTIALS_ID = "dockerhub-credentials"
        DOCKER_HOST_PORT = "7070"
    }
	
	
	
	stages {
		
		stage("Compile") {
            steps {
                sh "mvn compile"
            }
        }
		
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
		
		//Using Env
		stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image and tag it with build number
                  sh "docker build --no-cache -t ${IMAGE_NAME}:${BUILD_TAG_VERSION} ."
                }
            }
        }
	
		//Without using Env
		// stage('Build Docker Image') {
  //           steps {
  //               script {
  //                   // Build Docker image and tag it with build number
  //                   def imageTag = "${env.BUILD_NUMBER}"
  //                   sh "docker build -t yym-calcu-image:v1 ."
  //                   env.IMAGE_TAG = imageTag
  //               }
  //           }
  //       }
        
        //Old Ver
    //     stage('Run Docker Container') {
    //     steps {
    //         echo 'Running container locally (port 7070)...'
    //         sh '''
    //             docker stop yym-calcu-container || true
    //             docker rm yym-calcu-container || true
    //             docker run -d --name yym-calcu-conatiner -p 7070:8080 yym-calcu-image:v1
    //         '''
    //     }    
    // }

		//New Ver 
		stage("Run Docker Container") {
            steps {
                script {
                    // Stop/remove existing container if it exists
                    def containerExists = sh(script: "docker ps -aq -f name=${CONTAINER_NAME}", returnStdout: true).trim()
                    if (containerExists) {
                        sh "docker stop ${CONTAINER_NAME} || true"
                        sh "docker rm ${CONTAINER_NAME} || true"
                    }
                    // Run new container safely
                    def runStatus = sh(script: "docker run -d --name ${CONTAINER_NAME} -p 8082:8080 ${IMAGE_NAME}:${BUILD_TAG_VERSION}", returnStatus: true)
                    if (runStatus != 0) {
                        echo "⚠️ Docker run failed, check logs"
                    } else {
                        echo "✅ Docker container ${CONTAINER_NAME} deployed successfully"
                    }
                }
            }
        }
	}
	 post {
          always {
              echo "✅ Pipeline finished."
          }
          success {
             echo "Pipeline succeeded! App running at http://150.95.84.29:${env.DOCKER_HOST_PORT}/"
			  emailext(
            to: 'yyint3914@gmail.com',
			subject: '✅ Build SUCCESS',
			body: 'Build completed successfully.'
             )
          }
          failure {
              echo "Pipeline failed."
			  emailext(
                to: 'yyint3914@gmail.com',
                subject: '❌ Build FAILED',
                body: 'Build failed. Check logs.'
            )
          }
      }
}
