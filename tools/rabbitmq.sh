#
#   script to start the underlying infrastructure used by the order-service
#
#   the infra and users hard coded here are just for development purposes (local)
#

#####
#
# zipkin - http://localhost:9411/zipkin/?lookback=15m&endTs=1777520959144&limit=10
#
#docker run -d -p 9411:9411 openzipkin/zipkin    # create zipkin container
#docker start zipkin                   # start zipkin container already created

#####
#
# rabbitmq - http://localhost:15672/#/queues/%2F/order-created-queue
#
# admin:L@bracanto
# order:L@bracanto
#
docker start rabbitmq

#####
#
#  postgres - jdbc:postgresql://localhost:32768/orders
#
#  postgres:postgres
#
docker start postgres