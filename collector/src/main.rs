use std::env;
use std::error::Error;

use sqlx::postgres::PgPool;

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {

    dotenvy::dotenv()?;

    let database_url = env::var("DATABASE_URL")
        .expect("DATABASE_URL must be set in .env file");

    let pool = PgPool::connect(&database_url).await?;
    println!("connection success");
    Ok(())
}