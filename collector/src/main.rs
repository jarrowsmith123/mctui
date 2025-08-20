mod models;

use axum::{Json, Router, extract::State, http::StatusCode, routing::post};
use sqlx::postgres::PgPool;
use std::env;
use std::error::Error;
use uuid::Uuid;

// It's good practice to group your own modules together
use crate::models::GameEvent;

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {
    dotenvy::dotenv()?;

    let database_url = env::var("DATABASE_URL").expect("DATABASE_URL must be set in .env file");

    let pool = PgPool::connect(&database_url).await?;
    println!("connection success");

    let app = Router::new()
        .route("/events", post(event_handler))
        .with_state(pool);

    let addr: String = "0.0.0.0:3000".parse().unwrap();
    let listener = tokio::net::TcpListener::bind(addr.clone()).await?;
    println!("connected on {addr}");
    axum::serve(listener, app).await?;

    Ok(())
}

async fn event_handler(
    State(pool): State<PgPool>,
    Json(event): Json<GameEvent>,
) -> Result<StatusCode, StatusCode> {
    let event_id = Uuid::new_v4();
    let result = sqlx::query!(
        "INSERT INTO events (id, event_type, player_name, payload) VALUES ($1, $2, $3, $4)",
        event_id,
        event.event_type,
        event.player_name,
        event.payload
    )
    .execute(&pool)
    .await;
    match result {
        Ok(_) => Ok(StatusCode::CREATED),
        Err(_) => Err(StatusCode::INTERNAL_SERVER_ERROR),
    }
}
