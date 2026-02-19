import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration

// =====================
// PRODUCER - Kirim pesan ke Kafka
// =====================
Properties producerProps = new Properties()
producerProps.put("bootstrap.servers", "localhost:9092")
producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)
producer.send(new ProducerRecord<>("test-topic", "key1", "Hello from Katalon Producer"))
producer.flush()
producer.close()
println "✅ Message sent to Kafka topic."

// =====================
// CONSUMER - Baca pesan dari Kafka
// =====================
Properties consumerProps = new Properties()
consumerProps.put("bootstrap.servers", "localhost:9092")
consumerProps.put("group.id", "katalon-consumer-group")
consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
consumerProps.put("auto.offset.reset", "earliest")

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)
consumer.subscribe(["test-topic"])

boolean messageReceived = false
int retries = 5

for (int i = 0; i < retries; i++) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3))
    for (ConsumerRecord<String, String> record : records) {
        println "✅ Consumer received - Key: ${record.key()}, Value: ${record.value()}"
        assert record.value() != null
        messageReceived = true
        break
    }
    if (messageReceived) break
}

consumer.close()
assert messageReceived : "❌ No message received from Kafka!"
println "Kafka Consumer Test PASSED ✅"