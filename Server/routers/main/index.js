module.exports = (pool) => {
    const express = require('express');
    const router = express.Router();

    router.get('/items', async (req, res) => {
        const {query: {category_id, address_id}} = req
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT It_id, Name, Quick_price,'
                                            + ' Current_price, Create_date, Img'
                                            + ' FROM ITEM'
                                            + ' WHERE c_id = ?'
                                            + ' AND Ad_id = ?'
                                            + ' ORDER BY Create_date DESC', [category_id, address_id]);
            const item_list = result.map((item_info) => {
                return {
                    item_id: item_info.It_id,
                    name: item_info.name,
                    current_price: item_info.Current_price,
                    immediate_price: item_info.Quick_price,
                    created_date: item_info.Create_date.toLocaleDateString(),
                    img_url: item_info.Img
                }
            });
            res.status(200).json({
                item_list
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    return router;
};