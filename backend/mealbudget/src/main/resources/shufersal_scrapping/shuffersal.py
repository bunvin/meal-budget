#pip install selenium beautifulsoup4 lxml requests
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time
import re
import json
import os

def extract_from_URL(url, product_list=None, category=None):
        if product_list is None:
            product_list = []
        
        try:
            # Initialize Firefox driver
            driver = webdriver.Firefox()
            driver.get(url)

            # Wait for initial products to load
            WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.CLASS_NAME, "miglog-prod"))
            )

            # Scroll to load all products
            scroll_to_bottom(driver)

            # Get the updated page source after scrolling
            html_content = driver.page_source
            soup = BeautifulSoup(html_content, 'html.parser')
            products = soup.find_all('li', class_='miglog-prod')

            # Process all products
            for product in products:
                product_info = extract_product_info(product, category)
                if product_info:
                    product_list.append(product_info)

            # Print results
            print(f"Found {len(product_list)} products:")
            # for product in product_list:
            #     print(f"Name: {product['name']}")
            #     print(f"Selling Method: {product['selling_method']}")
            #     print(f"Price: {product['price']} NIS per {product['quantity']} {product['unit']}")
            #     print("-" * 50)

        except Exception as e:
            print(f"Error in processing: {e}")
        
        finally:
            driver.quit()
            
        return product_list


def scroll_to_bottom(driver):
    """Scroll to the bottom of the page until no new content loads"""
    last_height = driver.execute_script("return document.body.scrollHeight")
    
    while True:
        # Scroll to bottom
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        
        # Wait for new content to load
        time.sleep(2)
        
        # Calculate new scroll height
        new_height = driver.execute_script("return document.body.scrollHeight")
        
        # Break if no new content loaded
        if new_height == last_height:
            break
            
        last_height = new_height

def extract_product_info(product, category):
    """Extract information from a single product element"""
    try:
        name = product.get('data-product-name', '')
        selling_method = product.get('data-selling-method', '')
        
        # Get image URL
        img_element = product.find('img', class_='pic')
        img_url = img_element.get('src', '') if img_element else ''
        
        # getting the price
        flex_line = product.find('div', class_='flex-line')
        if flex_line:
            price_text = flex_line.find('div', class_='smallText')
            
            if price_text:
                # Get raw text for parsing
                raw_text = price_text.text.strip()
                
                # Extract price and unit using regex
                price_match = re.search(r'(\d+\.?\d*)\s*ש"ח\s*ל-\s*(\d+)\s*(ק"ג|גרם|יחידה|מ"ל)', raw_text)
                if price_match:
                    price = float(price_match.group(1))
                    quantity = int(price_match.group(2))
                    unit_raw = price_match.group(3)
                    unit = '' 
                    if unit_raw == 'ק"ג':
                        unit = 'kg'
                    elif unit_raw == 'גרם':
                        unit = 'gr'
                    elif unit_raw == 'יחידה':
                        # Skip products sold by unit with no weight
                        return None
                    elif unit_raw == 'מ"ל':
                        unit = 'ml'
                    else:
                        unit = unit_raw
                    
                    return {
                        'name': name,
                        'selling_method': selling_method,
                        'price': price,
                        'unit': unit,
                        'quantity': quantity,
                        'image_url': img_url,
                        'category': category
                    }
                else: 
                    print(f"No match for price text: '{raw_text}'")
            else:
                print(f"No price text found for product: {name}")
        else:
            print(f"No flex-line found for product: {name}")
            
    except Exception as e:
        print(f"Error processing product {name}: {e}")
    return None

def main():
    URL = { "Vegetables":"https://www.shufersal.co.il/online/he/קטגוריות/סופרמרקט/פירות-וירקות/c/A04?q=:relevance:categories-3:A0410",
           "MilkEggs":"https://www.shufersal.co.il/online/he/קטגוריות/סופרמרקט/חלב-וביצים/c/A01",
           "Meat":"https://www.shufersal.co.il/online/he/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%95%D7%AA/%D7%A1%D7%95%D7%A4%D7%A8%D7%9E%D7%A8%D7%A7%D7%98/%D7%9E%D7%95%D7%A6%D7%A8%D7%99-%D7%91%D7%A9%D7%A8%2C-%D7%A2%D7%95%D7%A3-%D7%95%D7%93%D7%92%D7%99%D7%9D-/%D7%91%D7%A9%D7%A8-%D7%91%D7%A7%D7%A8-%D7%95%D7%9B%D7%91%D7%A9/%D7%91%D7%A9%D7%A8-%D7%98%D7%A8%D7%99/c/A070201?shuf_source=shufersal_square_icon_dy&shuf_medium=Banner_text_A07&shuf_campaign=meat&shuf_content=shufersal_meat_121124&shuf_term=dy",
           "Chicken":"https://www.shufersal.co.il/online/he/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%95%D7%AA/%D7%A1%D7%95%D7%A4%D7%A8%D7%9E%D7%A8%D7%A7%D7%98/%D7%9E%D7%95%D7%A6%D7%A8%D7%99-%D7%91%D7%A9%D7%A8%2C-%D7%A2%D7%95%D7%A3-%D7%95%D7%93%D7%92%D7%99%D7%9D-/%D7%9E%D7%95%D7%A6%D7%A8%D7%99-%D7%A2%D7%95%D7%A3-%D7%95%D7%94%D7%95%D7%93%D7%95/%D7%A2%D7%95%D7%A3-%D7%98%D7%A8%D7%99-%D7%A9%D7%95%D7%A4%D7%A8%D7%A1%D7%9C/c/A071422?shuf_source=shufersal_square_icon_dy&shuf_medium=Banner_text_A07&shuf_campaign=chicken&shuf_content=shufersal_chicken_121124&shuf_term=dy",
           "Fish":"https://www.shufersal.co.il/online/he/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%95%D7%AA/%D7%A1%D7%95%D7%A4%D7%A8%D7%9E%D7%A8%D7%A7%D7%98/%D7%9E%D7%95%D7%A6%D7%A8%D7%99-%D7%91%D7%A9%D7%A8%2C-%D7%A2%D7%95%D7%A3-%D7%95%D7%93%D7%92%D7%99%D7%9D-/%D7%93%D7%92%D7%99%D7%9D/%D7%93%D7%92%D7%99%D7%9D-%D7%98%D7%A8%D7%99%D7%99%D7%9D/c/A070504?shuf_source=shufersal_square_icon_dy&shuf_medium=Banner_text_A07&shuf_campaign=fish&shuf_content=shufersal_fish_121124&shuf_term=dy",
           "BakingShimurim":"https://www.shufersal.co.il/online/he/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%95%D7%AA/%D7%A1%D7%95%D7%A4%D7%A8%D7%9E%D7%A8%D7%A7%D7%98/%D7%91%D7%99%D7%A9%D7%95%D7%9C-%D7%90%D7%A4%D7%99%D7%94-%D7%95%D7%A9%D7%99%D7%9E%D7%95%D7%A8%D7%99%D7%9D/c/A22",
           "Frozen":"https://www.shufersal.co.il/online/he/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%95%D7%AA/%D7%A1%D7%95%D7%A4%D7%A8%D7%9E%D7%A8%D7%A7%D7%98/%D7%9E%D7%96%D7%95%D7%9F-%D7%9E%D7%A7%D7%95%D7%A8%D7%A8-%D7%A7%D7%A4%D7%95%D7%90%D7%99%D7%9D-%D7%95%D7%A0%D7%A7%D7%A0%D7%99%D7%A7%D7%99%D7%9D/c/A16"
           }

    
    all_products = []
    
    for category, url in URL.items():
        print(f"\nProcessing category: {category}")
        all_products = extract_from_URL(url, all_products, category)
    
    # Save final results to JSON file
    current_path = os.getcwd() 
    output_path = os.path.join(current_path, 'backend\\mealbudget\\src\\main\\resources\\shufersal_scrapping\\shufersal_products.json')
    
    try:
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(all_products, f, ensure_ascii=False, indent=4)
        
        print(f"\nSaved a total of {len(all_products)} products to {output_path}")
    except Exception as e:
        print(f"Error saving JSON file: {e}")


if __name__ == "__main__":
    main()