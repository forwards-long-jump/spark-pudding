function getRequiredComponents()
  return {squares = {"size", "position", "color"}, directions = {"size", "position", "color", "direction"}}
end

function render(g)
  game.camera:applyTransforms(g)
  -- Squares
  for i, square in ipairs(squares) do
    g:setColor(game.color:fromRGB(square.color.r, square.color.g, square.color.b))
    g:fillRect(square.position.x, square.position.y, square.size.width, square.size.height)
  end

  for i, square in ipairs(directions) do
    squareSizeW = square.size.width / 4
    squareSizeH = square.size.height / 4
    g:setColor(game.color:fromRGB(square.color.r, square.color.g, square.color.b))
    if square.direction.current == "TOP" then
      g:fillRect(square.position.x + squareSizeW, square.position.y - squareSizeH,
        squareSizeW * 2, squareSizeH)
    elseif square.direction.current == "LEFT" then
      g:fillRect(square.position.x - squareSizeW, square.position.y + squareSizeH,
        squareSizeW, squareSizeH * 2)
    elseif square.direction.current == "RIGHT" then
      g:fillRect(square.position.x + square.size.width, square.position.y + squareSizeH,
        squareSizeW, squareSizeH * 2)
    elseif square.direction.current == "BOTTOM" then
      g:fillRect(square.position.x + squareSizeW, square.position.y + square.size.height,
        squareSizeW * 2, squareSizeH)
    end

    g:fillRect(square.position.x, square.position.y, square.size.width, square.size.height)
  end
  game.camera:resetTransforms(g)
end
