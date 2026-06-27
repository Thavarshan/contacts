import { expect, test } from '@playwright/test';

test.describe('public and auth pages', () => {
  test('moves from the landing page into auth entry points', async ({ page }) => {
    await page.goto('/');

    await expect(
      page.getByRole('heading', {
        level: 1,
        name: /manage every contact, conversation, and next step/i,
      }),
    ).toBeVisible();
    await expect(page.getByRole('link', { name: /contacts manager/i })).toHaveAttribute(
      'href',
      '/',
    );

    await page
      .getByRole('link', { name: /^sign in$/i })
      .first()
      .click();
    await expect(page).toHaveURL('/auth/login');
    await expect(
      page.getByRole('heading', { name: /login to your account/i }),
    ).toBeVisible();

    await page.getByRole('link', { name: /register/i }).click();
    await expect(page).toHaveURL('/auth/register');
    await expect(page.getByRole('heading', { name: /create an account/i })).toBeVisible();

    await page.goto('/auth/login');
    await page.getByRole('link', { name: /forgot your password/i }).click();
    await expect(page).toHaveURL('/auth/forgot-password');
    await expect(
      page.getByRole('heading', { name: /reset your password/i }),
    ).toBeVisible();
  });
});

test.describe('contacts dashboard', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/dashboard');
    await expect(page.getByRole('table')).toBeVisible();
  });

  test('searches and opens contact details', async ({ page }) => {
    await page.getByPlaceholder('Search contacts').fill('spring networking');

    await expect(page.getByText('Marcus Reed')).toBeVisible();
    await expect(page.getByText('Sarah Chen')).toBeHidden();

    await page.getByRole('button', { name: /marcus reed/i }).click();
    await expect(page.getByRole('heading', { name: /marcus reed/i })).toBeVisible();
    await expect(page.getByText(/principal at reed advisory/i)).toBeVisible();
  });

  test('creates a new contact', async ({ page }) => {
    await page
      .getByRole('button', { name: /add contact/i })
      .last()
      .click();

    await page.getByLabel('First name').fill('Avery');
    await page.getByLabel('Last name').fill('Stone');
    await page.getByLabel('Company').fill('Stone Studio');
    await page.getByLabel('Job title').fill('Designer');
    await page.getByLabel('Phone').fill('+1 (555) 555-0199');
    await page.getByLabel('Email').fill('avery.stone@example.com');
    await page.getByLabel('Street').fill('10 Main Street');
    await page.getByLabel('City').fill('Austin');
    await page.getByLabel('State').fill('TX');
    await page.getByLabel('Postal code').fill('78701');
    await page.getByLabel('Country').fill('United States');
    await page.getByLabel('Birthday').fill('1994-05-14');
    await page.getByLabel('Notes').fill('Asked for an import walkthrough.');
    await page.getByRole('button', { name: /create contact/i }).click();

    await expect(page.getByText('Avery Stone')).toBeVisible();
    await expect(page.getByText(/stone studio - designer/i)).toBeVisible();
  });

  test('edits and deletes contacts from the row action menu', async ({ page }) => {
    await page.getByPlaceholder('Search contacts').fill('Sarah Chen');
    await page.getByRole('button', { name: /open contact menu/i }).click();
    await page.getByRole('menuitem', { name: /^edit$/i }).click();
    await page.getByLabel('Company').fill('Northstar Partners');
    await page.getByRole('button', { name: /save changes/i }).click();

    await expect(page.getByText(/northstar partners - product lead/i)).toBeVisible();

    await page.getByPlaceholder('Search contacts').fill('Marcus Reed');
    await page.getByRole('button', { name: /open contact menu/i }).click();
    await page.getByRole('menuitem', { name: /^delete$/i }).click();
    await expect(page.getByRole('heading', { name: /delete contact/i })).toBeVisible();
    await page.getByRole('button', { name: /delete contact/i }).click();

    await expect(page.getByText('Marcus Reed')).toBeHidden();
    await expect(page.getByText(/no contacts found/i)).toBeVisible();
  });

  test('performs visible-row bulk actions', async ({ page }) => {
    await page
      .getByRole('checkbox', { name: /select all contacts on this page/i })
      .click();

    await expect(page.getByText(/8 contact\(s\) selected/i)).toBeVisible();
    await page.getByRole('button', { name: /clear/i }).click();
    await expect(page.getByText(/8 contact\(s\) selected/i)).toBeHidden();
  });
});
